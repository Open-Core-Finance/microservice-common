import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {environment} from "../../environments/environment";
import {Order, OrderDirection, Paging} from '../classes/Paging';
import {RestService} from '../services/rest.service';
import {GeneralApiResponse} from '../classes/GeneralApiResponse';
import {AuthenticationService} from "../services/authentication.service";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {UserMessage} from '../classes/UserMessage';
import {BehaviorSubject, Observable, Subscription} from "rxjs";
import {LanguageService} from "../services/language.service";
import {CommonService} from "../services/common.service";
import { LoginSession } from '../classes/LoginSession';
import {PageEvent} from '@angular/material/paginator';
import { UiOrderEvent } from '../classes/UiOrderEvent';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { OrganizationService } from '../services/organization.service';
import { ConfirmDialogComponent, ConfirmDialogModel } from '../confirm-dialog/confirm-dialog.component';
import { GeneralModel } from '../classes/CommonClasses';

@Component({
    template: ''
})
export abstract class TableComponent<T extends GeneralModel> implements AfterViewInit {

    public paging: Paging | null = null;
    public message: UserMessage = {success: [], error: []};
    protected limitOrderColumn = 1;
    public sortFields: Order[];
    protected customData: any;
    public searchText: string;
    public itemPerPage: number;
    public addMode: boolean = false;
    public addingItem: T | null = null;
    loginSession: LoginSession | null = null;
    pageEvent: PageEvent;
    pageSizeOptions = environment.pageSizeOptions;
    tableData: any[] = [];
    displayedColumns: string[] 
    isLoadingResults: boolean = true;

    public messageSubject: BehaviorSubject<UserMessage>;
    private messageObservable: Observable<UserMessage>;

    @ViewChild(MatSort) sort: MatSort = new MatSort();

    private _messageSubscription: Subscription | null = null;
    private _sessionSubscription: Subscription | null = null;
    private _languageSubscription: Subscription | null = null;
    private _organizationSubscription: Subscription | null = null;
    private _sortSubscription: Subscription | null = null;
    private _deleteSubscription: Subscription | null = null;

    public constructor(protected restService: RestService, public auth: AuthenticationService,
                          protected http: HttpClient, protected router: Router, public languageService: LanguageService,
                          protected commonService: CommonService, public dialog: MatDialog,
                          protected organizationService: OrganizationService) {
        this.messageSubject = new BehaviorSubject<UserMessage>({success: [], error: []});
        this.messageObservable = this.messageSubject.asObservable();
        this._messageSubscription = this.messageObservable.subscribe( m => this.message = m);
        this.clearMessages();
        this.sortFields = [];
        this.searchText = "";
        this.itemPerPage = environment.pageSize;
        this._sessionSubscription = this.auth.currentSession.subscribe(x =>  this.loginSession = x);
        this._languageSubscription = languageService.languageDataObservable.subscribe( languageData => this.refreshLanguage(languageData));
        this.pageEvent = this.emptyPageEvent();
        this.pageEvent.pageSize = environment.pageSize;
        this.displayedColumns = this.buildTableColumns();
        this.customData = {};
        this._organizationSubscription = this.organizationService.organizationObservable.subscribe(s => {
            if (s) this.customData['organizationId'] = s.id;
        });
    }

    ngAfterViewInit() {
        if (this.sort) {
          this._sortSubscription = this.sort.sortChange.subscribe( s => this.changeOrder({order: s}));
        }
        if (this.organizationService.organization?.id) {
            this.customData['organizationId'] = this.organizationService.organization.id
        }
    }

    ngOnDestroy() {
        this.unsubscribe(this._messageSubscription);
        this.unsubscribe(this._sessionSubscription);
        this.unsubscribe(this._languageSubscription);
        this.unsubscribe(this._organizationSubscription);
        this.unsubscribe(this._sortSubscription);
        this.unsubscribe(this._deleteSubscription);
    }

    reloadData() {
        this.getData(this.pageEvent.pageIndex, this.sortFields, this.customData);
    }

    clearMessages() {
        this.messageSubject.next({success: [], error: []});
    }

    getData(pageNumber: number, orders: Order[], customData: any) {
        this.isLoadingResults = true;
        this.clearMessages();
        this.paging = null;
        const headers = this.restService.initRequestHeaders();
        let body = {
            pageSize: this.itemPerPage,
            pageIndex: pageNumber,
            orders: JSON.stringify(orders),
            searchText: this.searchText
        };
        if (customData) {
            body = Object.assign(body, customData);
        }
        const requestBody = this.commonService.buildPostStringBody(body);
        this.http.post<GeneralApiResponse>(this.getSearchUrl(), requestBody, {headers})
            .subscribe({next: (data: GeneralApiResponse) => this.dataLoadSuccess(data as Paging), error: (data: any) => this.showError(data)});
    }

    protected dataLoadSuccess(paging: Paging) {
        this.isLoadingResults = false;
        this.paging = paging;
        if (this.paging) {
            this.tableData = [];
            if (this.paging.result) {
                var index = (this.paging.pageNumber * this.paging.pageSize) + 1
                for ( const item of this.paging.result) {
                    const obj = Object.assign(this.createNewItem(), item);
                    obj['index'] = index++;
                    this.tableData.push(obj);
                }
            }
            this.pageEvent.pageIndex = this.paging.pageNumber;
            this.pageEvent.pageSize = this.paging.pageSize;
            this.pageEvent.length = this.paging.totalElements;
        } else {
            this.pageEvent = this.emptyPageEvent();
        }
    }

    protected showError(data: any) {
        this.isLoadingResults = false;
        if (data.statusText) {
            this.message.error.push(data.statusText);
        } else if (data.statusCode) {
            this.message.error.push(data.statusCode);
        } else {
            this.message.error.push("Unknown error: " + JSON.stringify(data));
        }
        this.messageSubject.next(this.message);
    }

    handlePageEvent(e: PageEvent) {
        this.pageEvent = e;
        this.itemPerPage = this.pageEvent.pageSize;
        this.reloadData();
    }

    abstract getServiceUrl(): string;

    getDeleteUrl(): string {
        return this.getServiceUrl() + "/delete";
    }

    getSearchUrl(): string {
        return this.getServiceUrl() + "/";
    }

    getCreateUpdateUrl(): string {
        return this.getServiceUrl() + "/create-or-update";
    }

    getViewDetailsUrl(itemId: string): string {
        return this.getServiceUrl() + "/" + itemId;
    }

    changeOrder($event: any) {
        const order = $event.order as UiOrderEvent;
        if (this.sortFields == null) {
            this.sortFields = [];
        }
        for (let index = 0; index < this.sortFields.length; index++) {
            const orderField = this.sortFields[index];
            if (orderField.property === order.active) {
                this.sortFields.splice(index, 1);
                index--;
            }
        }
        while (this.limitOrderColumn > 0 && this.sortFields.length >= this.limitOrderColumn) {
            this.sortFields.pop();
        }
        let orderDirection = null;
        if (order.direction === "asc") {
            orderDirection = OrderDirection.ASC;
        } else {
            orderDirection = OrderDirection.DESC;
        }
        const newOrder = new Order(order.active, orderDirection);
        this.sortFields.unshift(newOrder);
        this.reloadData();
    }

    abstract getDeleteConfirmContent(item: T): string;
    abstract getDeleteConfirmTitle(item: T): string;

    showDeleteModel(item: T) {
        const message = this.getDeleteConfirmContent(item);
        const title = this.getDeleteConfirmTitle(item);
        const dialogData = new ConfirmDialogModel(title, message);
    
        const dialogRef = this.dialog.open(ConfirmDialogComponent, { data: dialogData });
    
        this._deleteSubscription = dialogRef.afterClosed().subscribe(dialogResult => {
          if (dialogResult) {
            const requestHeaders = this.restService.initApplicationJsonRequestHeaders();
            const serviceUrl = this.getDeleteUrl();
            this.http.delete<GeneralApiResponse>(serviceUrl, {
              headers: requestHeaders, params: { entityId: item.id }
            }).subscribe({
              next: (_: GeneralApiResponse) => this.reloadData(), error: (data: any) => this.showError(data)
            });
          }
        });
      }

    searchClick($event: any) {
        this.searchText = $event.searchText;
        this.reloadData();
    }

    cancelAdd($event: any) {
        this.addMode = false;
    }

    submitAdd($event: any) {
        this.createNewItem();
        this.addMode = false;
        this.reloadData();
    }

    addClick($event: any) {
        this.clearMessages();
        this.createNewItem();
        this.addingItem = this.createNewItem();
        this.addMode = true;
    }

    abstract createNewItem(): T;

    editClick($event: any, item: T) {
        this.addingItem = item;
        this.addMode = true;
        this.clearMessages();
    }

    refreshLanguage(languageData: Record<string, any>) {
    }

    abstract buildTableColumns(): string[];

    emptyPageEvent() {
        const result = new PageEvent();
        result.pageSize = environment.pageSize;
        result.pageIndex = 0;
        result.length = 0;
        return result;
    }

    protected unsubscribe( subscription: Subscription | null) {
        if (subscription) {
            subscription.unsubscribe();
        }
    }
}

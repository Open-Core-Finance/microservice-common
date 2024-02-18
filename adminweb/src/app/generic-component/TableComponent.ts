import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {environment} from "../../environments/environment";
import {Order, OrderDirection, Paging} from '../classes/Paging';
import {RestService} from '../services/rest.service';
import {GeneralApiResponse} from '../classes/GeneralApiResponse';
import {AuthenticationService} from "../services/authentication.service";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {UserMessage} from '../classes/UserMessage';
import {Subscription} from "rxjs";
import {LanguageService} from "../services/language.service";
import {CommonService} from "../services/common.service";
import { LoginSession } from '../classes/LoginSession';
import {PageEvent} from '@angular/material/paginator';
import { UiOrderEvent } from '../classes/UiOrderEvent';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { OrganizationService } from '../services/organization.service';
import { ConfirmDialogComponent, ConfirmDialogModel } from './confirm-dialog/confirm-dialog.component';
import { GeneralModel } from '../classes/CommonClasses';
import { PermissionService } from '../services/permission.service';
import { AccessControl, Permission, ResourceAction } from '../classes/Permission';

@Component({
    template: ''
})
export abstract class TableComponent<T extends GeneralModel<any>> implements AfterViewInit, OnInit {

    public static NO_LIST_PERMISSION_ERROR_CODE = "permission_list_error";

    public paging: Paging | null = null;
    public message: UserMessage = new UserMessage([], []);
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

    @ViewChild(MatSort) sort: MatSort = new MatSort();

    private _sessionSubscription: Subscription | null = null;
    private _languageSubscription: Subscription | null = null;
    private _organizationSubscription: Subscription | null | undefined = null;
    private _sortSubscription: Subscription | null = null;
    private _deleteSubscription: Subscription | null = null;
    private _permissionSubscription: Subscription | null = null;
    private _permissionConfigSubscription: Subscription | null = null;

    currentModulePermissions: Permission[] = [];

    public constructor(protected restService: RestService, public auth: AuthenticationService,
                          protected http: HttpClient, protected router: Router, public languageService: LanguageService,
                          protected commonService: CommonService, public dialog: MatDialog,
                          protected organizationService: OrganizationService,
                          protected permissionService: PermissionService, protected changeDetector: ChangeDetectorRef) {
        this.clearMessages();
        this.sortFields = [];
        this.searchText = "";
        this.itemPerPage = environment.pageSize;
        this.pageEvent = this.emptyPageEvent();
        this.pageEvent.pageSize = environment.pageSize;
        this.displayedColumns = this.buildTableColumns();
        this.customData = {};
    }

    ngOnInit(): void {
        this._sessionSubscription?.unsubscribe();
        this._sessionSubscription = this.auth.currentSessionSubject.subscribe(x =>  this.loginSession = x);
        this._languageSubscription?.unsubscribe();
        this._languageSubscription = this.languageService.languageDataSubject.subscribe( languageData => this.refreshLanguage(languageData));
        this._organizationSubscription?.unsubscribe();
        this._organizationSubscription = this.organizationService.organizationSubject.subscribe(s => {
            if (s) this.customData['organizationId'] = s.id;
        });
        this._permissionSubscription?.unsubscribe();
        this._permissionSubscription = this.permissionService.currentUserPermissionSubject.subscribe(p => this.permissionReloaded(p));
        this._permissionConfigSubscription?.unsubscribe();
        this._permissionConfigSubscription = this.permissionService.permissionConfigSubject.subscribe(p => this.permissionReloaded(this.permissionService.currentUserPermissions));
    }

    ngAfterViewInit() {
        this._sortSubscription?.unsubscribe();
        if (this.sort) {
          this._sortSubscription = this.sort.sortChange.subscribe( s => this.changeOrder({order: s}));
        }
        if (this.organizationService.organization?.id) {
            this.customData['organizationId'] = this.organizationService.organization.id
        }
    }

    ngOnDestroy() {
        this._sessionSubscription?.unsubscribe();
        this._languageSubscription?.unsubscribe();
        this._organizationSubscription?.unsubscribe();
        this._sortSubscription?.unsubscribe();
        this._deleteSubscription?.unsubscribe();
        this._permissionSubscription?.unsubscribe();
        this._permissionConfigSubscription?.unsubscribe();
    }

    reloadData() {
        this.getData(this.pageEvent.pageIndex, this.sortFields, this.customData);
    }

    clearMessages() {
        this.message.clearAll();
    }

    getData(pageNumber: number, orders: Order[], customData: any) {
        this.isLoadingResults = true;
        this.clearMessages();
        if (!this.canViewList()) {
            this.message.error.push(TableComponent.NO_LIST_PERMISSION_ERROR_CODE);
            this.changeDetector.detectChanges();
        }
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
            .subscribe({next: (data: GeneralApiResponse) => this.dataLoadSuccess(data as Paging),
                error: (data: any) => this.restService.handleRestError(data, this.message)});
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

    handlePageEvent(e: PageEvent) {
        this.pageEvent = e;
        this.itemPerPage = this.pageEvent.pageSize;
        this.reloadData();
    }

    abstract getServiceUrl(): string;

    getSearchUrl(): string {
        return this.getServiceUrl() + "/";
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
            const serviceUrl = this.getServiceUrl() + "/" + item.id;
            this.http.delete<GeneralApiResponse>(serviceUrl, {
              headers: requestHeaders, params: { entityId: item.id }
            }).subscribe({
              next: (_: GeneralApiResponse) => this.reloadData(),
              error: (data: any) => this.restService.handleRestError(data, this.message)
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
        if ($event.type == "updated") {
            this.message.success.push("update_successfull");
        } else {
            this.message.success.push("add_successfull");
        }
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

    abstract permissionResourceName(): string;

    permissionReloaded(permissions: Permission[]) {
        this.currentModulePermissions = [];
        for (let p of permissions) {
            if (this.getServiceUrl().indexOf(p.serviceUrl) >= 0 && p.resourceType.toLowerCase() == this.permissionResourceName().toLowerCase()) {
                this.currentModulePermissions.push(p);
            }
        }
        if (this.canViewList()) {
            var err = this.message.error;
            for(let i = 0; i < err.length; i++) {
                if (err[i] == TableComponent.NO_LIST_PERMISSION_ERROR_CODE) {
                    err.splice(i, 1);
                    i--;
                }
            }
        }
    }

    canViewList(): boolean {
        return this.canDoAction(ResourceAction.COMMON_ACTION_LIST);
    }

    canAddItem(): boolean {
        return this.canDoAction(ResourceAction.COMMON_ACTION_ADD);
    }

    canUpdateItem(): boolean {
        return this.canDoAction(ResourceAction.COMMON_ACTION_UPDATE);
    }

    canDeleteItem(): boolean {
        return this.canDoAction(ResourceAction.COMMON_ACTION_DELETE);
    }

    canDoAction(action: string) {
        const a: ResourceAction | null = this.permissionService.filterResourceActionByResourceNameAndAction(
            this.permissionService.currentPermissionConfigs, this.permissionResourceName(), action
        );
        if (a != null) {
            for (let p of this.currentModulePermissions) {
                if (this.permissionService.checkPatternMatched(a, p)) {
                    return p.control != AccessControl.DENIED;
                }
            }
        }
        return environment.defaultPermissionIfNull != AccessControl.DENIED;
    }
}

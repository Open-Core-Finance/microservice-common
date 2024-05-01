import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Paging } from 'src/app/classes/Paging';
import { UserMessage } from 'src/app/classes/UserMessage';
import { TableUi } from 'src/app/classes/ui/UiTableDisplay';
import { LanguageService } from 'src/app/services/language.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-generic-table',
  templateUrl: './generic-table.component.html',
  styleUrl: './generic-table.component.sass'
})
export class GenericTableComponent implements OnInit, OnDestroy {

  @Input()
  tableUi: TableUi = new TableUi("error.");
  
  @Input()
  message: UserMessage = new UserMessage([], []);

  @Input()
  paging: Paging | null = null;

  @Input()
  pageEvent: PageEvent | null = null;

  @Input()
  pageSizeOptions = environment.pageSizeOptions;

  @Output() reload = new EventEmitter();
  @Output() add = new EventEmitter();
  @Output() edit = new EventEmitter();
  @Output() delete = new EventEmitter();
  @Output() viewDetails = new EventEmitter();
  @Output() pageAction = new EventEmitter();

  @Input()
  rowActionTemplate: any;

  @Input()
  complexCellTemplate: any;

  constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  addClick($event: any) {
    this.add.emit($event);
  }

  reloadData() {
    this.reload.emit();
  }

  editClick($event: any, element: any) {
    this.edit.emit({event: $event, element: element});
  }

  deleteClick(element: any) {
    this.delete.emit(element);
  }

  viewDetailsClick(element: any) {
    this.viewDetails.emit(element);
  }

  handlePageEvent($event: any) {
    this.pageAction.emit($event);
  }

  retreiveSubField(element: any, subField: string, jsonout: boolean | null): any {
    if (!element) {
      return "";
    }
    if (subField) {
      const index = subField.indexOf('.');
      let out: any = "";
      if (index > 0) {
        let suffix = subField.substring(index + 1);
        let prefix = subField.substring(0, index);
        out = this.retreiveSubField(element[prefix], suffix, jsonout);
      } else {
        out = element[subField];
      }
      if (!jsonout) {
        return out;
      } else {
        return JSON.stringify(out);
      }
    } else {
      return element;
    }
  }
}

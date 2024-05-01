import { SortDirection } from "@angular/material/sort";

export class TableUi {
    loading: boolean  = false;
    errorPrefix: string = "error.";
    dataSource: any[] = [];
    addMode = false;
    sortActive = "id";
    sortDirection: SortDirection = "asc";
    indexColumnLabelKey = "numberInList";
    indexColumnClass = "no-column";
    enabledActionRefresh: Function = () => true;
    enabledActionEdit: Function = () => true;
    enabledActionAdd: Function = () => true;
    enabledActionDelete: Function = () => true;
    enabledActionCustom: Function = () => true;
    enabledActionViewDetails: Function = () => true;
    emptyDataMessageKey = "tableNoData";
    enabledBottomPaging: Function = () => true;
    enabledTopPaging: Function = () => true;
    columns: TableColumnUi[] = [];
    actionLabelKey = "action.action";

    constructor(errorPrefix: string) {
        this.errorPrefix = errorPrefix;
    }

    get enabledActionColumn() {
        const tableUi = this;
        return tableUi.enabledActionAdd || tableUi.enabledActionRefresh || tableUi.enabledActionAdd || 
          tableUi.enabledActionEdit || tableUi.enabledActionDelete || tableUi.enabledActionCustom;
    }

    get tableColumNames(): string[] {
        const tableUi = this;
        var result: string[] = [];
        if (tableUi.indexColumnLabelKey != '') {
        result.push("index");
        }
        for (let column of tableUi.columns) {
        result.push(column.columnName);
        }
        if (tableUi.enabledActionColumn) {
        result.push("action");
        }
        return result;
    }
}

export class TableColumnUi {
    columnName = "";
    columnClass = "";
    columnLabelKey = "";
    enableSort = true;
    /**
     * Supported transform:
     * 1. dateFormat: "yyyy-MM-dd"
     * 2. numberFormat: '1.0-2'
     * 3. plainTextDisplay: true (Default)
     * 4. function: (input) => {}
     * 5. labelPrefix: "display.column_"
     * 6. complex: true => Other display can set by html template for complexCellTemplate[column.columnName]
     * 7. subField: childattr, jsonout: false
     */
    columnTransformOptions: any = {
        plainTextDisplay: true
    };

    constructor(columnName: string, columnLabelKey: string, columnTransformOptions?: any) {
        this.columnName = columnName;
        this.columnLabelKey = columnLabelKey;
        if (columnTransformOptions) {
            this.columnTransformOptions = columnTransformOptions;
        }
    }
}
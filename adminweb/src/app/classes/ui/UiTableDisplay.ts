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
    enabledActionRefresh = true;
    enabledActionEdit = true;
    enabledActionAdd = true;
    enabledActionDelete = true;
    enabledActionCustom = true;
    emptyDataMessageKey = "tableNoData";
    enabledBottomPaging = true;
    enabledTopPaging = true;
    columns: TableColumnUi[] = [];
    actionLableKey = "action.action";

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
     * 1. dateFormat = "yyyy-MM-dd"
     * 2. numberFormat = '1.0-2'
     * 3. plainTextDisplay = true (Default)
     * 4. function = (input) => {}
     * 5. labelPrefix = "display.column_"
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
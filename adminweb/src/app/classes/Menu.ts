import { LabelProvider } from "./LabelProvider";
import { LanguageItem } from "./LanguageItem";

export class MenuGroup {
    id: string;
    items: MenuItem[];
    labelProvider: LabelProvider;
    visibleFn: Function | null;

    constructor(id: string, labelProvider: LabelProvider, items: MenuItem[], visibleFn: Function | null) {
        this.id = id;
        this.labelProvider = labelProvider;
        this.items = items;
        this.visibleFn = visibleFn;
    }

    get label(): string {
        return this.labelProvider.getLabel();
    }
}

export class MenuItem {
    /**
     * Page URL that this item will load.
     */
    activateUrl: string;
    /**
     * Key for localize label.
     */
    labelProvider: LabelProvider;
    /**
     * Only use this url if iconName is empty.
     */
    iconUrl: string;
    /** 
     * Only use this name whenever iconUrl is empty;
     */
    iconName: string;

    activated: boolean  = true;

    visibleFn: Function | null;

    constructor(activateUrl: string, labelProvider: LabelProvider, iconUrl: string, iconName: string, visibleFn: Function | null) {
        this.activateUrl = activateUrl;
        this.labelProvider = labelProvider;
        this.iconUrl = iconUrl;
        this.iconName = iconName;
        this.visibleFn = visibleFn;
    }

    get label(): string {
        return this.labelProvider.getLabel();
    }
}

export class ActionMenuItem extends MenuItem {
    action: Function | null;
    constructor(labelProvider: LabelProvider, iconUrl: string, iconName: string, action: Function | null, visibleFn: Function | null) {
        super("", labelProvider, iconUrl, iconName, visibleFn);
        this.action = action;
    }
}

export class LanguageMenuItem extends ActionMenuItem {
    languageItem: LanguageItem;

    constructor(labelProvider: LabelProvider, iconName: string, languageItem: LanguageItem, visibleFn: Function | null) {
        super(labelProvider, "", iconName, null, visibleFn);
        this.languageItem = languageItem;
    }
}
import { LabelProvider } from "./LabelProvider";
import { LanguageItem } from "./LanguageItem";

export class MenuGroup {
    id: string;
    items: MenuItem[];
    labelProvider: LabelProvider;

    constructor(id: string, labelProvider: LabelProvider, items: MenuItem[]) {
        this.id = id;
        this.labelProvider = labelProvider;
        this.items = items;
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

    constructor(activateUrl: string, labelProvider: LabelProvider, iconUrl: string, iconName: string) {
        this.activateUrl = activateUrl;
        this.labelProvider = labelProvider;
        this.iconUrl = iconUrl;
        this.iconName = iconName;
    }

    get label(): string {
        return this.labelProvider.getLabel();
    }
}

export class ActionMenuItem extends MenuItem {
    action: Function | null;
    constructor(labelProvider: LabelProvider, iconUrl: string, iconName: string, action: Function | null) {
        super("", labelProvider, iconUrl, iconName);
        this.action = action;
    }
}

export class LanguageMenuItem extends ActionMenuItem {
    languageItem: LanguageItem;

    constructor(labelProvider: LabelProvider, iconName: string, languageItem: LanguageItem) {
        super(labelProvider, "", iconName, null);
        this.languageItem = languageItem;
    }
}
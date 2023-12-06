export class LanguageItem {
    languageKey: string;
    name: string;
    data: any;
    defaultLanguage: boolean = false;

    constructor(languageKey: string, name: string) {
        this.languageKey = languageKey;
        this.name = name;
    }

}

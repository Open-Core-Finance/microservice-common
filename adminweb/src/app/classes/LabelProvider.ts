import { LanguageService } from "../services/language.service";
import { OrganizationService } from "../services/organization.service";
import { Organization } from "./Organization";

export interface LabelProvider {
    getLabel(): string;
}

export class LanguageKeyLabelProvider implements LabelProvider {
    labelKey: string;
    labelParameters: any[];
    languageData: Record<string, any> = {};

    constructor(private languageService: LanguageService, labelKey: string, labelParameters: any[]) {
        this.labelKey = labelKey;
        this.labelParameters = labelParameters;
        languageService.languageDataObservable.subscribe(languageData => this.refreshLanguage(languageData));
    }

    getLabel(): string {
        return this.languageService.formatLanguage(this.labelKey, this.labelParameters);
    }

    refreshLanguage(languageData: Record<string, any>) {
        this.languageData = languageData;
    }
}

export class OrganizationNameLabelProvider implements LabelProvider {
    organization: Organization | null = null;

    constructor(private organizationService: OrganizationService) {
        organizationService.organizationObservable.subscribe( organization => this.organization = organization);
    }

    getLabel(): string {
        if (this.organization != null) {
            return this.organization.name;
        } else {
            return "";
        }
    }
}

export class StaticTextLabelProvider implements LabelProvider {
    constructor(private lebel: string) {}

    getLabel(): string {
        return this.lebel;
    }
}
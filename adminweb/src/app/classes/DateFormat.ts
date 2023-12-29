import { Injectable } from "@angular/core"
import { LanguageService } from "../services/language.service"

@Injectable()
export class CustomDateFormat {

    constructor(private languageService: LanguageService){}

    get display(){
        var result = {
            dateInput: this.languageService.formatLanguage("dateFormatValue", []),
            monthYearLabel: this.languageService.formatLanguage("monthYearLabel", []),
            dateA11yLabel: this.languageService.formatLanguage("dateA11yLabel", []),
            monthYearA11yLabel: this.languageService.formatLanguage("monthYearA11yLabel", []),
        }
        return result;
    }

    get parse(){
        return {
            dateInput: this.languageService.formatLanguage("dateFormatValue", [])
        }
    }
}
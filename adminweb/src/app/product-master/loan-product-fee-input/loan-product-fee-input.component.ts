import { Component, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Currency } from 'src/app/classes/Currency';
import { LoanFeePaymentType, LoanProductFee } from 'src/app/classes/products/LoanProduct';
import { MonthlyPayOption } from 'src/app/classes/products/Product';
import { LoanProductFeeType } from 'src/app/classes/products/ProductFeeType';
import { LanguageService } from 'src/app/services/language.service';

@Component({
    selector: 'app-loan-product-fee-input',
    templateUrl: './loan-product-fee-input.component.html',
    styleUrl: './loan-product-fee-input.component.sass',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => LoanProductFeeInputComponent),
            multi: true
        }
    ],
    standalone: false
})
export class LoanProductFeeInputComponent implements OnInit, ControlValueAccessor, OnDestroy {
  isDisabled: boolean = false;
  selectedFees: LoanProductFee[] = [];
  productFeeTypeEnum = LoanProductFeeType;
  allProductFeeTypes = Object.keys(LoanProductFeeType);
  allFeePaymentTypes = Object.keys(LoanFeePaymentType);
  feePaymentType = LoanFeePaymentType;
  _supportedCurrencies: Currency[] = [];

  public constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  writeValue(value: LoanProductFee[]): void {
    this.selectedFees = value;
  }
 
  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.propagateTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }

  propagateChange = (_: LoanProductFee[]) => { };
  propagateTouched = (_: LoanProductFee[]) => { };

  @Input()
  set supportedCurrencies(supportedCurrencies: Currency[]) {
    this._supportedCurrencies = supportedCurrencies;
  }

  get supportedCurrencies(): Currency[] {
    return this._supportedCurrencies;
  }

  addFeeClick() {
    var item = new LoanProductFee();
    if (this._supportedCurrencies.length > 1) {
      item.currencyId = this._supportedCurrencies[1].id;
    } else if (this._supportedCurrencies.length > 0) {
      item.currencyId = this._supportedCurrencies[0].id;
    }
    this.selectedFees.push(item);
  }

  removeFee($event: any, index: number) {
    this.selectedFees.splice(index, 1);
  }

  typeChanged($event: any, index: number) {
    let selectedFee: LoanProductFee = this.selectedFees[index];
    if (selectedFee != null && $event == LoanProductFeeType.PLANNED) {
      selectedFee.feePaymentType = LoanFeePaymentType.FLAT;
    }
  }

  showFeeApplicaiton(index: number): boolean {
    let selectedFee: LoanProductFee = this.selectedFees[index];
    if (selectedFee != null) {
      const listSupportApplicaiton: LoanProductFeeType[] = [
        LoanProductFeeType.DEDUCTED_DISBURSEMENT, LoanProductFeeType.CAPITALIZED_DISBURSEMENT,
        LoanProductFeeType.UPFRONT_DISBURSEMENT, LoanProductFeeType.PAYMENT_DUE_DUE_DATE,
        LoanProductFeeType.PAYMENT_DUE_UPFRONT
      ];
      return listSupportApplicaiton.indexOf(selectedFee.type) >= 0;
    }
    return false;
  }

  getListSupportPaymentTypes(index: number): LoanFeePaymentType[] {
    let selectedFee: LoanProductFee = this.selectedFees[index];
    if (selectedFee != null) {
      switch(selectedFee.type) {
        case LoanProductFeeType.MANUAL_FEE:
        case LoanProductFeeType.DEDUCTED_DISBURSEMENT:
        case LoanProductFeeType.CAPITALIZED_DISBURSEMENT:
        case LoanProductFeeType.UPFRONT_DISBURSEMENT:
          return [LoanFeePaymentType.FLAT, LoanFeePaymentType.LOAN_AMOUNT_PERCENTAGE];
        case LoanProductFeeType.LATE_REPAYMENT:
          return [LoanFeePaymentType.FLAT, LoanFeePaymentType.LOAN_AMOUNT_PERCENTAGE,
            LoanFeePaymentType.REPAYMENT_PRINCIPAL_AMOUNT_PERCENTAGE];
        case LoanProductFeeType.PAYMENT_DUE_UPFRONT:
          return [LoanFeePaymentType.FLAT, LoanFeePaymentType.FLAT_NUMBER_OF_INSTALLMENTS,
            LoanFeePaymentType.LOAN_AMOUNT_PERCENTAGE, LoanFeePaymentType.LOAN_AMOUNT_PERCENTAGE_NUMBER_OF_INSTALLMENTS];
        case LoanProductFeeType.PAYMENT_DUE_DUE_DATE:
          return [LoanFeePaymentType.FLAT, LoanFeePaymentType.FLAT_NUMBER_OF_INSTALLMENTS,
            LoanFeePaymentType.LOAN_AMOUNT_PERCENTAGE, LoanFeePaymentType.LOAN_AMOUNT_PERCENTAGE_NUMBER_OF_INSTALLMENTS];
        default: return [LoanFeePaymentType.FLAT];
      }
    }
    return [];
  }

  showPaymentType(index: number, type: any) {
    return this.getListSupportPaymentTypes(index).indexOf(type) >= 0;
  }
}

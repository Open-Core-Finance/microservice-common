import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanProductFeeInputComponent } from './loan-product-fee-input.component';

describe('DepositProductFeeInputComponent', () => {
  let component: LoanProductFeeInputComponent;
  let fixture: ComponentFixture<LoanProductFeeInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanProductFeeInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoanProductFeeInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

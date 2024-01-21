import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanPaymentCollectionInputComponent } from './loan-payment-collection-input.component';

describe('LoanPaymentCollectionInputComponent', () => {
  let component: LoanPaymentCollectionInputComponent;
  let fixture: ComponentFixture<LoanPaymentCollectionInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanPaymentCollectionInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoanPaymentCollectionInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

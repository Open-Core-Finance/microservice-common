import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanRepaymentSchedulingInputComponent } from './loan-repayment-scheduling-input.component';

describe('LoanRepaymentSchedulingInputComponent', () => {
  let component: LoanRepaymentSchedulingInputComponent;
  let fixture: ComponentFixture<LoanRepaymentSchedulingInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanRepaymentSchedulingInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoanRepaymentSchedulingInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

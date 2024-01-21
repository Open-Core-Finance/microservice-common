import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanInterestRateInputComponent } from './loan-interest-rate-input.component';

describe('LoanInterestRateInputComponent', () => {
  let component: LoanInterestRateInputComponent;
  let fixture: ComponentFixture<LoanInterestRateInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanInterestRateInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoanInterestRateInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

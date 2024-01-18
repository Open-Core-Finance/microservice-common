import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepositInterestRateInputComponent } from './deposit-interest-rate-input.component';

describe('DepositInterestRateInputComponent', () => {
  let component: DepositInterestRateInputComponent;
  let fixture: ComponentFixture<DepositInterestRateInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepositInterestRateInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DepositInterestRateInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

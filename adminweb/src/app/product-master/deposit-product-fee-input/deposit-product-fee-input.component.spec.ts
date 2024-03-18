import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepositProductFeeInputComponent } from './deposit-product-fee-input.component';

describe('DepositProductFeeInputComponent', () => {
  let component: DepositProductFeeInputComponent;
  let fixture: ComponentFixture<DepositProductFeeInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepositProductFeeInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DepositProductFeeInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepositLimitInputComponent } from './deposit-limit-input.component';

describe('DepositLimitInputComponent', () => {
  let component: DepositLimitInputComponent;
  let fixture: ComponentFixture<DepositLimitInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepositLimitInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DepositLimitInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

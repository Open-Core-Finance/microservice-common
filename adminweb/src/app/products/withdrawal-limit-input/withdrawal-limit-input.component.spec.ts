import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WithdrawalLimitInputComponent } from './withdrawal-limit-input.component';

describe('WithdrawalLimitInputComponent', () => {
  let component: WithdrawalLimitInputComponent;
  let fixture: ComponentFixture<WithdrawalLimitInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WithdrawalLimitInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WithdrawalLimitInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

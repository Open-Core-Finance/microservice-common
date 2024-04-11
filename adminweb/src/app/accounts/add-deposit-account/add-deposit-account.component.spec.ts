import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddDepositAccountComponent } from './add-deposit-account.component';

describe('AddDepositAccountComponent', () => {
  let component: AddDepositAccountComponent;
  let fixture: ComponentFixture<AddDepositAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddDepositAccountComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddDepositAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLoanProductComponent } from './add-loan-product.component';

describe('AddDepositProductComponent', () => {
  let component: AddLoanProductComponent;
  let fixture: ComponentFixture<AddLoanProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddLoanProductComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddLoanProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

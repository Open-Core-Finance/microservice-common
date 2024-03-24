import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCorporateCustomerComponent } from './add-corporate-customer.component';

describe('AddCorporateCustomerComponent', () => {
  let component: AddCorporateCustomerComponent;
  let fixture: ComponentFixture<AddCorporateCustomerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCorporateCustomerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddCorporateCustomerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

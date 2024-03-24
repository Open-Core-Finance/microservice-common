import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddIndividualCustomerComponent } from './add-individual-customer.component';

describe('AddIndividualCustomerComponent', () => {
  let component: AddIndividualCustomerComponent;
  let fixture: ComponentFixture<AddIndividualCustomerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddIndividualCustomerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddIndividualCustomerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

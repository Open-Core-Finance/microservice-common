import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddExchangeRateComponent } from './add-exchange-rate.component';

describe('AddExchangeRateComponent', () => {
  let component: AddExchangeRateComponent;
  let fixture: ComponentFixture<AddExchangeRateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddExchangeRateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddExchangeRateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

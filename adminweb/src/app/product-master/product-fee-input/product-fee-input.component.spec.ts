import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductFeeInputComponent } from './product-fee-input.component';

describe('ProductFeeInputComponent', () => {
  let component: ProductFeeInputComponent;
  let fixture: ComponentFixture<ProductFeeInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductFeeInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductFeeInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

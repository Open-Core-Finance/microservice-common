import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCryptoProductComponent } from './add-crypto-product.component';

describe('AddDepositProductComponent', () => {
  let component: AddCryptoProductComponent;
  let fixture: ComponentFixture<AddCryptoProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCryptoProductComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddCryptoProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

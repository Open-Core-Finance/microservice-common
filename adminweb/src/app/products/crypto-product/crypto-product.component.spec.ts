import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CryptoProductComponent } from './crypto-product.component';

describe('DepositProductComponent', () => {
  let component: CryptoProductComponent;
  let fixture: ComponentFixture<CryptoProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CryptoProductComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CryptoProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

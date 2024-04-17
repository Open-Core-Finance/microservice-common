import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCryptoAccountComponent } from './add-crypto-account.component';

describe('AddCryptoAccountComponent', () => {
  let component: AddCryptoAccountComponent;
  let fixture: ComponentFixture<AddCryptoAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCryptoAccountComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddCryptoAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

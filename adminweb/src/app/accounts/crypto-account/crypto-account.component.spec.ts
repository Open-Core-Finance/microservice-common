import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CryptoAccountComponent } from './crypto-account.component';

describe('CryptoAccountComponent', () => {
  let component: CryptoAccountComponent;
  let fixture: ComponentFixture<CryptoAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CryptoAccountComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CryptoAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

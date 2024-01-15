import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GlProductComponent } from './gl-product.component';

describe('DepositProductComponent', () => {
  let component: GlProductComponent;
  let fixture: ComponentFixture<GlProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GlProductComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GlProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddGlProductComponent } from './add-gl-product.component';

describe('AddDepositProductComponent', () => {
  let component: AddGlProductComponent;
  let fixture: ComponentFixture<AddGlProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddGlProductComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddGlProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

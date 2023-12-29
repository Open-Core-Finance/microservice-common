import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRateSourceComponent } from './add-rate-source.component';

describe('AddRateSourceComponent', () => {
  let component: AddRateSourceComponent;
  let fixture: ComponentFixture<AddRateSourceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRateSourceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddRateSourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

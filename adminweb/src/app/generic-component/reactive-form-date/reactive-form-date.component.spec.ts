import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReactiveFormDateComponent } from './reactive-form-date.component';

describe('ReactiveFormInputComponent', () => {
  let component: ReactiveFormDateComponent;
  let fixture: ComponentFixture<ReactiveFormDateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormDateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReactiveFormDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

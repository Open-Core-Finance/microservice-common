import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReactiveFormCheckboxComponent } from './reactive-form-checkbox.component';

describe('ReactiveFormInputComponent', () => {
  let component: ReactiveFormCheckboxComponent;
  let fixture: ComponentFixture<ReactiveFormCheckboxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormCheckboxComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReactiveFormCheckboxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

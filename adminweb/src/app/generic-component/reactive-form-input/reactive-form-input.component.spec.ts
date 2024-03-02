import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReactiveFormInputComponent } from './reactive-form-input.component';

describe('ReactiveFormInputComponent', () => {
  let component: ReactiveFormInputComponent;
  let fixture: ComponentFixture<ReactiveFormInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReactiveFormInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

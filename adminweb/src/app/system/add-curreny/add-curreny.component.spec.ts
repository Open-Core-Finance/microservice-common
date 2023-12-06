import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCurrenyComponent } from './add-curreny.component';

describe('AddCurrenyComponent', () => {
  let component: AddCurrenyComponent;
  let fixture: ComponentFixture<AddCurrenyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCurrenyComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddCurrenyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

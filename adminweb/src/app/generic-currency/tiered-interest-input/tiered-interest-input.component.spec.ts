import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TieredInterestInputComponent } from './tiered-interest-input.component';

describe('TieredInterestInputComponent', () => {
  let component: TieredInterestInputComponent;
  let fixture: ComponentFixture<TieredInterestInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TieredInterestInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TieredInterestInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

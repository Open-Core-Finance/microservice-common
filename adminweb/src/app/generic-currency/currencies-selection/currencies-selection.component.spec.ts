import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrenciesSelectionComponent } from './currencies-selection.component';

describe('BranchesSelectionComponent', () => {
  let component: CurrenciesSelectionComponent;
  let fixture: ComponentFixture<CurrenciesSelectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurrenciesSelectionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CurrenciesSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

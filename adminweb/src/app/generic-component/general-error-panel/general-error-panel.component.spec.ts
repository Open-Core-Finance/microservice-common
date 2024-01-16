import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GeneralErrorPanelComponent } from './general-error-panel.component';

describe('GeneralErrorPanelComponent', () => {
  let component: GeneralErrorPanelComponent;
  let fixture: ComponentFixture<GeneralErrorPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GeneralErrorPanelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GeneralErrorPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

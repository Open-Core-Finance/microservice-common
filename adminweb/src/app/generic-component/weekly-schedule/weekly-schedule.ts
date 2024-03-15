import {Component, forwardRef, OnInit, ViewChild, ElementRef, OnDestroy, Output, EventEmitter} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import {LanguageService} from "../../services/language.service";
import { SharedModule } from 'src/app/generic-component/SharedModule';
import { CommonModule } from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import { CourseScheduleDto } from 'src/app/classes/registration/Course';
import { DayOfWeek } from 'src/app/classes/DayOfWeek';

@Component({
  selector: 'app-weekly-schedule',
  templateUrl: './weekly-schedule.component.html',
  styleUrl: './weekly-schedule.component.sass',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => WeeklyScheduleComponent),
      multi: true
    }
  ],
  standalone: true,
  imports: [CommonModule, SharedModule, MatFormFieldModule, MatAutocompleteModule, FormsModule, ReactiveFormsModule,
    MatIconModule, MatInputModule]
})
export class WeeklyScheduleComponent implements OnInit, ControlValueAccessor, OnDestroy {

  selectedSubjectDtoDisplay: CourseScheduleDto[] = [];
  daysOfWeek: DayOfWeek[] = Object.values(DayOfWeek);
  _selectedDay: DayOfWeek | null = null;
  lastWriteValues: CourseScheduleDto[] | undefined;

  @Output() change = new EventEmitter();

  @ViewChild("selectionInput") input: ElementRef | undefined = undefined;

  _formGroup: any | undefined;

  constructor(public languageService: LanguageService) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.propagateTouched = fn;
  }

  writeValue(value: CourseScheduleDto[]): void {
    this.lastWriteValues = value;
    this.populateValue(value);
  }

  private populateValue(value: CourseScheduleDto[] | undefined) {
    this.selectedSubjectDtoDisplay.splice(0, this.selectedSubjectDtoDisplay.length);
    if (value !== undefined) {
      value.forEach( d => this.selectedSubjectDtoDisplay.push(d));
    }
  }

  propagateChange = (_: CourseScheduleDto[]) => { };
  propagateTouched = (_: CourseScheduleDto[]) => { };

  set selectedDay(selectedDay: DayOfWeek | null) {
    if (this.input) {
      this.input.nativeElement.value = "";
    }
    if (selectedDay == null) {
      return;
    }
    let contain = false;
    for (let sb of this.selectedSubjectDtoDisplay) {
      if (sb.dayOfWeek == selectedDay) contain = true;
    }
    if (!contain && selectedDay) {
      const schedule = new CourseScheduleDto();
      schedule.dayOfWeek = selectedDay;
      this.selectedSubjectDtoDisplay.push(schedule);
      this.triggerChanged();
    }
    this._selectedDay = null;
  }

  get selectedDay(): DayOfWeek | null {
    return this._selectedDay;
  }

  isSelectedDay(dayOfWeek: DayOfWeek) {
    for (const b of this.selectedSubjectDtoDisplay) {
      if (dayOfWeek == b.dayOfWeek) return true;
    }
    return false;
  }

  removeSchedule(b: CourseScheduleDto) {
    let found = false;
    for (let i = 0; i < this.selectedSubjectDtoDisplay.length; i++) {
      const sb = this.selectedSubjectDtoDisplay[i];
      if (sb.dayOfWeek == b.dayOfWeek) {
        this.selectedSubjectDtoDisplay.splice(i--, 1);
        found = true;
      }
    }
    if (found) {
      this.triggerChanged();
    }
    if (this.input) {
      this.input.nativeElement.value = "";
    }
    this._selectedDay = null;
  }

  triggerChanged() {
    this.propagateChange(this.selectedSubjectDtoDisplay);
    if (this.change) {
      this.change.emit(this.selectedSubjectDtoDisplay);
    }
  }
}

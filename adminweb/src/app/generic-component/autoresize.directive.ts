import { Directive, HostListener, ElementRef, Renderer2, AfterViewInit } from '@angular/core';

@Directive({
    selector: 'textarea[autoresize]',
    standalone: false
})
export class AutoResizeDirective implements AfterViewInit {

  constructor(private elementRef: ElementRef, private renderer: Renderer2) {}

  ngAfterViewInit(): void {
    this.adjust();
  }

  @HostListener('input', ['$event.target'])
  onInput(textArea: HTMLTextAreaElement): void {
    this.adjust();
  }

  @HostListener('ngModelChange', ['$event'])
  onModelChange(newValue: string): void {
    this.adjust();
  }

  adjust(): void {
    const textArea = this.elementRef.nativeElement;
    this.renderer.setStyle(textArea, 'overflow', 'scroll');
    this.renderer.setStyle(textArea, 'height', 'auto');
    this.renderer.setStyle(textArea, 'height', textArea.scrollHeight + 'px');
  }
}
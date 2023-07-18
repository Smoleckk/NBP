import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/components/dialog/dialog.component';
import { CurrentRequest } from 'src/app/models/currentRequest';
import { CurrentResponse } from 'src/app/models/currentResponse';
import { CurrentService } from 'src/app/services/current.service';

@Component({
  selector: 'app-currencies',
  templateUrl: './currencies.component.html',
  styleUrls: ['./currencies.component.css'],
})
export class CurrenciesComponent {
  constructor(
    private formBuilder: FormBuilder,
    private currentService: CurrentService,
    private dialog: MatDialog
  ) {}

  currencies = [
    'EUR',
    'THB',
    'USD',
    'AUD',
    'HKD',
    'CAD',
    'NZD',
    'SGD',
    'HUF',
    'CHF',
    'GBP',
    'UAH',
    'JPY',
    'CZK',
    'DKK',
    'ISK',
    'NOK',
    'SEK',
    'RON',
    'BGN',
    'TRY',
    'ILS',
    'CLP',
    'PHP',
    'MXN',
    'ZAR',
    'BRL',
    'MYR',
    'IDR',
    'INR',
    'KRW',
    'CNY',
    'XDR',
  ];

  currentRequest = this.formBuilder.group({
    currency: ['EUR', [Validators.required, Validators.pattern('^[A-Za-z]+$')]],
    name: [
      'Tom Cruse',
      [Validators.required, Validators.pattern('^[A-Za-z ]+$')],
    ],
  });

  saveForm() {
    if (this.currentRequest.valid) {
      const requestData: CurrentRequest = {
        currency: this.currentRequest.value.currency || '',
        name: this.currentRequest.value.name || '',
      };

      this.currentService.getCurrent(requestData).subscribe(
        (res: CurrentResponse) => {
          this.dialog.open(DialogComponent, {
            data: { currency: requestData.currency, value: res.value },
          });
        },
        (error) => {
          console.log('Wystąpił błąd:', error);
        }
      );
    } else {
      console.log('Sprawdź poprawność formularza');
    }
  }
}

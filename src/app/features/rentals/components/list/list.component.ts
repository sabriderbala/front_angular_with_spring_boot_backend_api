import { Component } from '@angular/core';
import { User } from 'src/app/interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { RentalsService } from '../../services/rentals.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { RentalsResponse } from '../../interfaces/api/rentalsResponse.interface';
import { Rental } from '../../interfaces/rental.interface';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {
  rentalsData: Rental[] = [];
  name: string = '';
  owner_id: number = 0;
  createdAt: Date = new Date();
  updatedAt: Date = new Date();
  description: string = '';
  picture: string = '';

  public rentals$ = this.rentalsService.all();

  constructor(
    private sanitizer: DomSanitizer,
    private sessionService: SessionService,
    private rentalsService: RentalsService
  ) {
    this.rentals$.subscribe((data: RentalsResponse) => {
      if (data) {
        for (const rental of Object.values(data)) {
          const rentalData = rental as Rental;
          this.name = rentalData.name;
          this.owner_id = rentalData.owner_id;
          this.createdAt = rentalData.createdAt;
          this.updatedAt = rentalData.updatedAt;
          this.description = rentalData.description;
          this.picture = rentalData.picture;
          this.rentalsData.push(rentalData);
        }
      }
    });
  }

  get user(): User | undefined {
    return this.sessionService.user;
  }

  getPictureUrl(picture: string): SafeUrl {
    const base64Image = 'data:image/jpeg;base64,' + picture;
    return this.sanitizer.bypassSecurityTrustUrl(base64Image);
  }
}

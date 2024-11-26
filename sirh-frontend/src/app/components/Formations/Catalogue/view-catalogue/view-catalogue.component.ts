import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatalogueServiceService } from '../../../../_services/catalogue-service.service';
import { switchMap, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-view-catalogue',
  templateUrl: './view-catalogue.component.html',
  styleUrls: ['./view-catalogue.component.scss'],
})
export class ViewCatalogueComponent implements OnInit {
  catalogueId: string;
  catalogueTitle: string;
  trainings: any[] = [];
  errorMessage: string = '';

  // Course images
  courseImages: string[] = [
    'assets/Avatars/course1.jpg',
    'assets/Avatars/course2.jpg',
    'assets/Avatars/course3.jpg',
    'assets/Avatars/course4.jpg',
  ];

  constructor(
    private route: ActivatedRoute,
    private catalogueService: CatalogueServiceService
  ) {}

  ngOnInit() {
    // Subscribe to route parameter changes
    this.route.params.pipe(distinctUntilChanged()).subscribe((params) => {
      this.catalogueId = params['id'];
      console.log('Route param id:', this.catalogueId);
      this.fetchCatalogueData();
    });
  }

  fetchCatalogueData() {
    console.log('Fetching data for catalogueId:', this.catalogueId);
    this.catalogueService.findByCatalogueId(this.catalogueId).subscribe(
      (response) => {
        console.log('Received response:', response);
        this.catalogueTitle = response.title;
        this.trainings = response.formations || [];
        this.assignCourseImages();
        this.errorMessage = '';
      },
      (error) => {
        console.error('Error fetching catalogue data:', error);
        this.errorMessage = 'Erreur lors du chargement du catalogue.';
      }
    );
  }

  assignCourseImages() {
    // Assign an image from the `courseImages` array to each training
    this.trainings.forEach((training, index) => {
      const imageIndex = index % this.courseImages.length;
      training.imageUrl = this.courseImages[imageIndex];
    });
  }
}

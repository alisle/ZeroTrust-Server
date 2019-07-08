import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MatButtonModule, MatCardModule, MatCheckboxModule, MatDialogModule, MatFormFieldModule,
  MatIconModule, MatInputModule,
  MatListModule, MatPaginatorModule, MatProgressBarModule, MatProgressSpinnerModule,
  MatSidenavModule, MatSnackBarModule, MatSortModule,
  MatTableModule, MatTabsModule,
  MatToolbarModule
} from "@angular/material";

@NgModule({
  declarations: [],
  imports: [
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatTabsModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatCheckboxModule,
  ],
  exports: [
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatTabsModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatCheckboxModule,
  ]
})
export class MaterialModule { }

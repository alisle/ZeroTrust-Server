import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {LogWriter} from "../../log-writer";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {Network} from "../../_model/Network";
import {NetworkNewDTO} from "../../_model/NetworkNewDTO";

@Component({
  selector: 'app-network-add',
  templateUrl: './network-add.dialog.html',
  styleUrls: ['./network-add.dialog.css']
})
export class NetworkAddDialog implements OnInit {
  private log: LogWriter = new LogWriter("network-add-dialog");

  public nameFormControls = new FormControl('', [
    Validators.required,
    Validators.maxLength(1024)
  ]);

  public noFormControls = new FormControl('', [
    Validators.maxLength(4096)
  ]);

  public addressFormControls = new FormControl('', [
    Validators.pattern("^([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])$"),
    Validators.required
  ]);

  public maskFormControls = new FormControl('', [
    Validators.pattern("^([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])$"),
    Validators.required
  ]);

  constructor(
    public dialogRef: MatDialogRef<NetworkAddDialog>,
    @Inject(MAT_DIALOG_DATA) public dto : NetworkNewDTO
  ) {
    this.log.debug("creating network-add dialog");
  }

  ngOnInit() {
  }

  enabled() : boolean {
    return !(this.nameFormControls.hasError("required") ||
           this.noFormControls.hasError("maxLength") ||
           this.addressFormControls.hasError("pattern") ||
           this.addressFormControls.hasError("required") ||
           this.maskFormControls.hasError("pattern") ||
           this.maskFormControls.hasError("required"));

  }

  cancel() : void {
    this.dialogRef.close();
  }
}

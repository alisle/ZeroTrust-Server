export class LogWriter {

  constructor(private header: string) {

  }

  error(output : string, obj: object = null) {
    if(obj != null) {
      console.error(`ERROR::${this.header}::${output}`, obj);
    } else {
      console.error(`ERROR::${this.header}::${output}`);
    }
  }

  warn(output: string, obj = null) {
    if(obj != null) {
      console.warn(`WARN::${this.header}::${output}`, obj);
    } else {
      console.warn(`WARN::${this.header}::${output}`);
    }
  }

  debug(output: string, obj: object = null) {
    if (obj != null) {
      console.debug(`DEBUG::${this.header}::${output}`, obj);
    } else {
      console.debug(`DEBUG::${this.header}::${output}`);
    }

  }


  info(output: string, obj : object = null) {
    if(obj != null) {
      console.info(`INFO::${this.header}::${output}`, obj);
    } else {
      console.info(`INFO::${this.header}::${output}`);
    }
  }
}

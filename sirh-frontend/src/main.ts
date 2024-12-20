import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { registerLicense } from '@syncfusion/ej2-base';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
registerLicense('Ngo9BigBOggjHTQxAR8/V1NDaF5cWWtCf1FpQ3xbf1x0ZFRMY1xbQHVPIiBoS35RckRiWXlfeHFcQ2dfUkN2');

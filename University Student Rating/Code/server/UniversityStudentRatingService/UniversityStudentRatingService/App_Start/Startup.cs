using DataAccessLayer.DbContext;
using DataAccessLayer.Identity;
using Microsoft.AspNet.Identity;
using Microsoft.Owin;
using Microsoft.Owin.Security.Cookies;
using Owin;
using ServiceLogicLayer.Interfaces;
using ServiceLogicLayer.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

[assembly: OwinStartup(typeof(UniversityStudentRatingService.App_Start.Startup))]
namespace UniversityStudentRatingService.App_Start
{
    public class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            app.CreatePerOwinContext(USR_DBEntities.Create);
            app.CreatePerOwinContext<ApplicationRoleManager>(ApplicationRoleManager.Create);
            app.CreatePerOwinContext<ApplicationUserManager>(ApplicationUserManager.Create);
            app.CreatePerOwinContext<IServiceCreator>(CreateServiceCreator);

            app.UseCookieAuthentication(new CookieAuthenticationOptions
            {
                AuthenticationType = DefaultAuthenticationTypes.ApplicationCookie,
                LoginPath = new PathString("/Account/Login"),
            });
        }

        private IServiceCreator CreateServiceCreator()
        {
            return new ServiceCreator(HttpContext.Current.GetOwinContext());
        }
    }
}
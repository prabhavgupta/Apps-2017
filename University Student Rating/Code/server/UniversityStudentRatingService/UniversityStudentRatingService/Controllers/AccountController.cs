using Microsoft.Owin.Security;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using ServiceLogicLayer.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using ServiceLogicLayer.DTO;
using AutoMapper;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.Web.Helpers;
using ServiceLogicLayer.Models.AccountModels;

namespace UniversityStudentRatingService.Controllers
{
    [RoutePrefix("api/Account")]
    public class AccountController : ApiController
    {
        private IServiceCreator services;
        private IAuthenticationManager authManager;

        public AccountController()
        {
            services = HttpContext.Current.GetOwinContext().GetUserManager<IServiceCreator>();
            authManager = HttpContext.Current.GetOwinContext().Authentication;
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("IsAuthenticated")]
        public bool IsAuthenticated()
        {
            return User.Identity.IsAuthenticated;
        }

        [AllowAnonymous]
        [HttpPost]
        [Route("Login")]
        public async Task<HttpResponseMessage> Login([FromBody]LoginModel model)
        {

            if (model == null)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Login or password is incorrect");
            }
            if (ModelState.IsValid)
            {
                var claim = await services.AuthService.Authenticate(model);
                
                if (claim != null)
                {
                    authManager.SignIn(new AuthenticationProperties { IsPersistent = true }, claim);
                    return new HttpResponseMessage(HttpStatusCode.OK);
                }
                else
                {
                    return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Login or password is incorrect");
                }
            }
            IEnumerable<String> errors = ModelState.Values.SelectMany(m => m.Errors).Select(e => e.ErrorMessage);
            return Request.CreateErrorResponse(HttpStatusCode.BadRequest, String.Join("\n", errors));
        }

        [Authorize]
        [HttpPost]
        [Route("Logout")]
        public HttpResponseMessage Logout()
        {
            try
            {
                authManager.SignOut();
                return new HttpResponseMessage(HttpStatusCode.OK);
            }
            catch
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Logout error");
            }
        }

        [AllowAnonymous]
        [HttpPost]
        [Route("Register")]
        public async Task<HttpResponseMessage> Register([FromBody]RegisterModel model)
        {
            if (model == null)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Register error");
            }
            if (ModelState.IsValid)
            {
                var result = await services.AuthService.CreateAsync(model);
     
                if (result.Succeeded)
                {
                    var claim = await services.AuthService.Authenticate(new LoginModel { UserName = model.UserName, Password = model.Password });
                    authManager.SignIn(new AuthenticationProperties { IsPersistent = true }, claim);
                    return new HttpResponseMessage(HttpStatusCode.OK);
                }
                else
                {
                    return Request.CreateErrorResponse(HttpStatusCode.BadRequest, String.Join("\n", result.Errors));
                }
            }
            IEnumerable<String> errors = ModelState.Values.SelectMany(m => m.Errors).Select(e => e.ErrorMessage);
            return Request.CreateErrorResponse(HttpStatusCode.BadRequest, String.Join("\n", errors));
        }
    }
}
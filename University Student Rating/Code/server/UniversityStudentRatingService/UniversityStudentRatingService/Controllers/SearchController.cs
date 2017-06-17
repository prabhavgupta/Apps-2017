using DataAccessLayer.DbContext;
using ServiceLogicLayer.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using Microsoft.Owin.Security;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Models.AccountModels;
using ServiceLogicLayer.Models;

namespace UniversityStudentRatingService.Controllers
{
    [RoutePrefix("api/Search")]
    public class SearchController : ApiController
    {
        private IServiceCreator services;

        public SearchController()
        {
            services = HttpContext.Current.GetOwinContext().GetUserManager<IServiceCreator>();
        }

        [AllowAnonymous]
        [HttpPost]
        [Route("SearchUsers")]
        public List<UserProfile> SearchUsers(SearchModel model)
        {
            if (model != null)
            {
                return services.SearchService.SearchUsers(model).ToList();
            }
            return null;
        }

        [AllowAnonymous]
        [HttpPost]
        [Route("SearchConcurents")]
        public List<ConcurentResultDTO> SearchConcurents(ConcurentModel model)
        {
            if (model != null)
            {
                return services.SearchService.SearchConcurents(model, services.StatisticService).ToList();
            }
            return null;
        }
    }
}

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
using DataAccessLayer.DbContext;
using ServiceLogicLayer.Models;

namespace UniversityStudentRatingService.Controllers
{
    [RoutePrefix("api/User")]
    public class UserController : ApiController
    {
        private IServiceCreator services;
        private IAuthenticationManager authManager;

        public UserController()
        {
            services = HttpContext.Current.GetOwinContext().GetUserManager<IServiceCreator>();
            authManager = HttpContext.Current.GetOwinContext().Authentication;
        }

        [HttpGet]
        [Route("GetUserProfileById")]
        public UserProfile GetUserProfileById(int id)
        {
            return services.UserService.GetById(id);
        }

        [HttpGet]
        [Route("GetUserProfiles")]
        public List<UserProfile> GetUserProfiles()
        {
            return services.UserService.GetAll().ToList(); ;
        }

        [HttpGet]
        [Route("GetStudentUserProfiles")]
        public List<UserProfile> GetStudentUserProfiles(int universityId, int facultyId, int courseId)
        {
            return services.UserService.GetStudentUserProfiles(universityId, facultyId, courseId).ToList();
        }

        [HttpGet]
        [Route("GetStudentUserProfilesByProfDisc")]
        public List<UserProfile> GetStudentUserProfilesByProfDisc(int universityId, int facultyId, int courseId, int professorId, int discipineId)
        {
            return services.UserService.GetStudentUserProfiles(universityId, facultyId, courseId, professorId, discipineId).ToList();
        }

        [Authorize]
        [HttpGet]
        [Route("GetAuthUserProfile")]
        public AuthUserDTO GetAuthUserProfile()
        {
            return services.UserService.GetAuthUserProfile(User.Identity.Name);
        }

        [Authorize]
        [HttpPost]
        [Route("SaveStudentProfessorDisciplines")]
        public bool SaveStudentProfessorDisciplines(SaveStudentProfessorDiscModel model)
        {
            return services.UserService.SaveStudentProfessorDisciplines(model);
        }

        [Authorize]
        [HttpPost]
        [Route("SaveProfessorDisciplines")]
        public bool SaveProfessorDisciplines(SaveProfessorDisciplinesModel model)
        {
            return services.UserService.SaveProfessorDisciplines(model);
        }
    }
}
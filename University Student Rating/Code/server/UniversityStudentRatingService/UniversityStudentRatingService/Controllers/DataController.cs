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
using ServiceLogicLayer.Models.AccountModels;
using ServiceLogicLayer.DTO;
using AutoMapper;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.Web.Helpers;
using DataAccessLayer.DbContext;
using System.Net.Http.Headers;
using System.IO;
using ServiceLogicLayer.Models;
using System.Drawing;
using System.Drawing.Imaging;

namespace UniversityStudentRatingService.Controllers
{
    [RoutePrefix("api/Data")]
    public class DataController : ApiController
    {
        private IServiceCreator services;

        public DataController()
        {
            services = HttpContext.Current.GetOwinContext().GetUserManager<IServiceCreator>();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetGenderTypes")]
        public List<GenderType> GetGenderTypes()
        {
            return services.DataService.GetGenderTypes().ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetCountries")]
        public List<Country> GetCountries()
        {
            return services.DataService.GetCountries().ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetCitiesByCountry")]
        public List<City> GetCitiesByCountry(int countryId)
        {
            return services.DataService.GetCitiesByCountry(countryId).ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetUniversitiesByCity")]
        public List<University> GetUniversitiesByCity(int cityId)
        {
            return services.DataService.GetUniversitiesByCity(cityId).ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetStatuses")]
        public List<Status> GetStatuses()
        {
            return services.DataService.GetStatuses().ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetFacultiesByUniversity")]
        public List<Faculty> GetFacultiesByUniversity(int universityId)
        {
            return services.DataService.GetFacultiesByUniversity(universityId).ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetCourses")]
        public List<Course> GetCourses()
        {
            return services.DataService.GetCourses().ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetImageFile")]
        public HttpResponseMessage GetImageFile(string filePath, string format)
        {
            string rootPath = HttpContext.Current.Server.MapPath("~/App_Data");
            string path = Path.Combine(rootPath, filePath);
            HttpResponseMessage result = new HttpResponseMessage(HttpStatusCode.OK);
            var stream = new FileStream(path, FileMode.Open, FileAccess.Read);
            result.Content = new StreamContent(stream);
            result.Content.Headers.ContentType = new MediaTypeHeaderValue("image/" + format);
            return result;
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetStudentProfessorDisciplines")]
        public List<StudentProfessorDiscipline> GetStudentProfessorDisciplines(int studentId)
        {
            return services.DataService.GetStudentProfessorDisciplines(studentId).ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetBranchProfessorDiscipline")]
        public BranchProfessorDisciplineDTO GetBranchProfessorDiscipline(int studentId, int universityId)
        {
            return services.DataService.GetBranchProfessorDiscipline(studentId, universityId);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetProfessorDisciplines")]
        public List<ProfessorDiscipline> GetProfessorDisciplines(int professorId)
        {
            return services.DataService.GetProfessorDisciplines(professorId).ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetDisciplinesByProfessor")]
        public List<Discipline> GetDisciplinesByProfessor(int professorId)
        {
            return services.DataService.GetDisciplinesByProfessor(professorId).ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetRatingDivisions")]
        public List<RatingDivision> GetRatingDivisions()
        {
            return services.DataService.GetRatingDivisions().ToList();
        }

        [Authorize]
        [HttpPost]
        [Route("UploadPhotoImageFile")]
        public HttpResponseMessage UploadPhotoImageFile([FromBody]UploadImageModel model)
        {
            try
            {
                string targetFolder = HttpContext.Current.Server.MapPath("~/App_Data");
                string fullPath = targetFolder + "/" + model.FilePath;

                byte[] imageBytes = Convert.FromBase64String(model.Base64Image);

                Image image;
                using (MemoryStream ms = new MemoryStream(imageBytes))
                {
                    image = Image.FromStream(ms);
                }

                Bitmap bitmap = new Bitmap(image);
                bitmap.Save(fullPath, System.Drawing.Imaging.ImageFormat.Jpeg);

                services.UserService.UpdateUserProfilePhotoPath(User.Identity.Name, model.FilePath);
                return Request.CreateResponse(HttpStatusCode.OK);
            }
            catch(Exception e){
                return Request.CreateErrorResponse(HttpStatusCode.InternalServerError, e);
            }
        }
    }
}
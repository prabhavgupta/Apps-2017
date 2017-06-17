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


namespace UniversityStudentRatingService.Controllers
{
    [RoutePrefix("api/Statistic")]
    public class StatisticController : ApiController
    {
        private IServiceCreator services;

        public StatisticController()
        {
            services = HttpContext.Current.GetOwinContext().GetUserManager<IServiceCreator>();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetDisciplineBranches")]
        public List<DisciplineBranch> GetDisciplineBranches()
        {
            return services.StatisticService.GetDisciplineBranches().ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetDisciplinesByBranch")]
        public List<Discipline> GetDisciplinesByBranch(int branchId)
        {
            return services.StatisticService.GetDisciplinesByBranch(branchId).ToList();
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetProfileRating")]
        public ProfileRatingDTO GetProfileRating(int studentId, int courseId)
        {
            return services.StatisticService.GetProfileRating(studentId, courseId);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetIndividualRatingByYear")]
        public IndividualRatingDTO GetIndividualRatingByYear(int studentId, int disciplineId, string academicYear)
        {
            return services.StatisticService.GetIndividualRating(studentId, disciplineId, academicYear);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetIndividualRating")]
        public IndividualRatingDTO GetIndividualRating(int studentId, int disciplineId)
        {
            return services.StatisticService.GetIndividualRating(studentId, disciplineId);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetAverageClassTopStudentsBy")]
        public List<IndividualRatingDTO> GetAverageClassTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount)
        {
            return services.StatisticService.GetAverageClassTopStudentsBy(universityId, courseId, disciplineId, topCount);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetOverallTopStudentsBy")]
        public List<IndividualRatingDTO> GetOverallTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount)
        {
            return services.StatisticService.GetOverallTopStudentsBy(universityId, courseId, disciplineId, topCount);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetOlympiadsTopStudentsBy")]
        public List<IndividualRatingDTO> GetOlympiadsTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount)
        {
            return services.StatisticService.GetOlympiadsTopStudentsBy(universityId, courseId, disciplineId, topCount);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetRatingType")]
        public RatingType GetRatingType(int universityId, int disciplineId)
        {
            return services.StatisticService.GetRatingType(universityId, disciplineId);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetAverageRatingForProfessor")]
        public double GetAverageRatingForProfessor(int professorId)
        {
            return services.StatisticService.GetAverageRatingForProfessor(professorId);
        }

        [AllowAnonymous]
        [HttpGet]
        [Route("GetRatingsForProfessorByDiscipline")]
        public List<Rating> GetRatingsForProfessorByDiscipline(int professorId, int disciplineId, int skipCount, int takeCount)
        {
            return services.StatisticService.GetRatingsForProfessorByDiscipline(professorId, disciplineId, skipCount, takeCount, services.UserService).ToList();
        }

        [Authorize]
        [HttpPost]
        [Route("SetRating")]
        public bool SetRating(Rating rating)
        {
            return services.StatisticService.SetRating(rating);
        }

        [Authorize]
        [HttpPost]
        [Route("DeleteRatingByIds")]
        public bool DeleteRatingByIds(List<int> ratingIds)
        {
            return services.StatisticService.DeleteRatingByIds(ratingIds);
        }
    }
}
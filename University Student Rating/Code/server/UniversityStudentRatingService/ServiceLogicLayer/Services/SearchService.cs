using DataAccessLayer.DbContext;
using DataAccessLayer.Interfaces;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Interfaces;
using ServiceLogicLayer.Models;
using ServiceLogicLayer.Models.AccountModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Services
{
    public class SearchService : ISearchService
    {
        IUnitOfWork Database { get; set; }
        delegate void TotalRatingDelegate(int studentId);  

        public SearchService(IUnitOfWork uow)
        {
            Database = uow;
        }

        public IEnumerable<UserProfile> SearchUsers(SearchModel model)
        {
            return Database.UserRepository.Get(u =>
                ((model.FirstName != "" && model.FirstName != null) ? u.FirstName == model.FirstName : true) &&
                ((model.LastName != "" && model.LastName != null) ? u.LastName == model.LastName : true) &&
                ((model.GenderTypeId != null) ? u.GenderTypeId == model.GenderTypeId : true) &&
                ((model.CityId != null) ? u.CityId == model.CityId : true) &&
                ((model.CountryId != null) ? u.CountryId == model.CountryId : true) &&
                ((model.CourseId != null) ? u.CourseId == model.CourseId : true) &&
                ((model.FacultyId != null) ? u.FacultyId == model.FacultyId : true) &&
                ((model.StatusId != null) ? u.StatusId == model.StatusId : true) &&
                ((model.UniversityId != null) ? u.UniversityId == model.UniversityId : true), u => u.Id, model.SkipCount, model.TakeCount);
        }


        public IEnumerable<ConcurentResultDTO> SearchConcurents(ConcurentModel model, IStatisticService statisticService)
        {
            IEnumerable<UserProfile> allStudents = Database.UserRepository.Find(u => 
                u.UniversityId == model.UniversityId &&
                (model.FacultyId != null ? u.FacultyId == model.FacultyId : true) &&
                (model.CourseId != null ? u.CourseId == model.CourseId : true));

            if (model.DisciplineBranchId == null)
            {
                if (model.StudentRatingType == StudentRatingType.Average)
                {
                    return GetConcurents(model, allStudents, (Func<int, int, double>)statisticService.GetAverageRatingByDivision, 1);
                }
                else if (model.StudentRatingType == StudentRatingType.Overall)
                {
                    return GetConcurents(model, allStudents, (Func<int, int>)statisticService.GetTotalRating);
                }
                else if (model.StudentRatingType == StudentRatingType.Olympiads)
                {
                    return GetConcurents(model, allStudents, (Func<int, int, int>)statisticService.GetTotalRatingByDivision, 2);
                }
            }
            else if (model.DisciplineId == null)
            {
                if (model.StudentRatingType == StudentRatingType.Average)
                {
                    return GetConcurents(model, allStudents, (Func<int, int, int, double>)statisticService.GetAverageRatingByBranchDivision, 1);
                }
                else if (model.StudentRatingType == StudentRatingType.Overall)
                {
                    return GetConcurents(model, allStudents, statisticService.GetTotalRatingByBranch);
                }
                else if (model.StudentRatingType == StudentRatingType.Olympiads)
                {
                    return GetConcurents(model, allStudents, (Func<int, int, int, int>)statisticService.GetTotalRatingByBranchDivision, 2);
                }
            }
            else
            {
                if (model.StudentRatingType == StudentRatingType.Average)
                {
                    return GetConcurents(model, allStudents, (Func<int, int, int, int, double>)statisticService.GetAverageRatingByBranchDisciplineDivision, 1);
                }
                else if (model.StudentRatingType == StudentRatingType.Overall)
                {
                    return GetConcurents(model, allStudents, statisticService.GetTotalRatingByBranchDiscipline);
                }
                else if (model.StudentRatingType == StudentRatingType.Olympiads)
                {
                    return GetConcurents(model, allStudents, (Func<int, int, int, int, int>)statisticService.GetTotalRatingByBranchDisciplineDivision, 2);
                }
            }
            return null;
        }


        private IEnumerable<ConcurentResultDTO> GetConcurents(ConcurentModel model, IEnumerable<UserProfile> allStudents, Func<int, int, int> GetTotalRatingFunc, int divisionId)
        {
            List<ConcurentResultDTO> concurents = new List<ConcurentResultDTO>();
            double total = GetTotalRatingFunc(model.StudentId, divisionId);

            if (model.SkipCount < 0)
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingFunc(u.Id, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue > total).OrderBy(c => c.RatingValue).Skip(Math.Abs(model.SkipCount + model.TakeCount)).Take(model.TakeCount).OrderByDescending(c => c.RatingValue).ToList();
            }
            else
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingFunc(u.Id, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue <= total).OrderByDescending(c => c.RatingValue).Skip(model.SkipCount).Take(model.TakeCount).ToList();
            }

            return concurents.AsEnumerable();
        }

        private IEnumerable<ConcurentResultDTO> GetConcurents(ConcurentModel model, IEnumerable<UserProfile> allStudents, Func<int, int> GetTotalRatingFunc)
        {
            List<ConcurentResultDTO> concurents = new List<ConcurentResultDTO>();
            double total = GetTotalRatingFunc(model.StudentId);

            if (model.SkipCount < 0)
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingFunc(u.Id) });
                }
                concurents = concurents.Where(c => c.RatingValue > total).OrderBy(c => c.RatingValue).Skip(Math.Abs(model.SkipCount + model.TakeCount)).Take(model.TakeCount).OrderByDescending(c => c.RatingValue).ToList();
            }
            else
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingFunc(u.Id) });
                }
                concurents = concurents.Where(c => c.RatingValue <= total).OrderByDescending(c => c.RatingValue).Skip(model.SkipCount).Take(model.TakeCount).ToList();
            }

            return concurents.AsEnumerable();
        }

        private IEnumerable<ConcurentResultDTO> GetConcurents(ConcurentModel model, IEnumerable<UserProfile> allStudents, Func<int, int, double> GetTotalRatingFunc, int divisionId)
        {
            List<ConcurentResultDTO> concurents = new List<ConcurentResultDTO>();
            double total = GetTotalRatingFunc(model.StudentId, divisionId);

            if (model.SkipCount < 0)
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingFunc(u.Id, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue > total).OrderBy(c => c.RatingValue).Skip(Math.Abs(model.SkipCount + model.TakeCount)).Take(model.TakeCount).OrderByDescending(c => c.RatingValue).ToList();
            }
            else
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingFunc(u.Id, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue <= total).OrderByDescending(c => c.RatingValue).Skip(model.SkipCount).Take(model.TakeCount).ToList();
            }

            return concurents.AsEnumerable();
        }
   

        private IEnumerable<ConcurentResultDTO> GetConcurents(ConcurentModel model, IEnumerable<UserProfile> allStudents, Func<int, int, int, int> GetTotalRatingByBranchFunc, int divisionId)
        {
            List<ConcurentResultDTO> concurents = new List<ConcurentResultDTO>();
            double total = GetTotalRatingByBranchFunc(model.StudentId, model.DisciplineBranchId.Value, divisionId);

            if (model.SkipCount < 0)
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchFunc(u.Id, model.DisciplineBranchId.Value, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue > total).OrderBy(c => c.RatingValue).Skip(Math.Abs(model.SkipCount + model.TakeCount)).Take(model.TakeCount).OrderByDescending(c => c.RatingValue).ToList();
            }
            else
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchFunc(u.Id, model.DisciplineBranchId.Value, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue <= total).OrderByDescending(c => c.RatingValue).Skip(model.SkipCount).Take(model.TakeCount).ToList();
            }

            return concurents.AsEnumerable();
        }

        private IEnumerable<ConcurentResultDTO> GetConcurents(ConcurentModel model, IEnumerable<UserProfile> allStudents, Func<int, int, int> GetTotalRatingByBranchFunc)
        {
            List<ConcurentResultDTO> concurents = new List<ConcurentResultDTO>();
            double total = GetTotalRatingByBranchFunc(model.StudentId, model.DisciplineBranchId.Value);

            if (model.SkipCount < 0)
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchFunc(u.Id, model.DisciplineBranchId.Value) });
                }
                concurents = concurents.Where(c => c.RatingValue > total).OrderBy(c => c.RatingValue).Skip(Math.Abs(model.SkipCount + model.TakeCount)).Take(model.TakeCount).OrderByDescending(c => c.RatingValue).ToList();
            }
            else
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchFunc(u.Id, model.DisciplineBranchId.Value) });
                }
                concurents = concurents.Where(c => c.RatingValue <= total).OrderByDescending(c => c.RatingValue).Skip(model.SkipCount).Take(model.TakeCount).ToList();
            }

            return concurents.AsEnumerable();
        }

        private IEnumerable<ConcurentResultDTO> GetConcurents(ConcurentModel model, IEnumerable<UserProfile> allStudents, Func<int, int, int, double> GetTotalRatingByBranchFunc, int divisionId)
        {
            List<ConcurentResultDTO> concurents = new List<ConcurentResultDTO>();
            double total = GetTotalRatingByBranchFunc(model.StudentId, model.DisciplineBranchId.Value, divisionId);

            if (model.SkipCount < 0)
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchFunc(u.Id, model.DisciplineBranchId.Value, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue > total).OrderBy(c => c.RatingValue).Skip(Math.Abs(model.SkipCount + model.TakeCount)).Take(model.TakeCount).OrderByDescending(c => c.RatingValue).ToList();
            }
            else
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchFunc(u.Id, model.DisciplineBranchId.Value, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue <= total).OrderByDescending(c => c.RatingValue).Skip(model.SkipCount).Take(model.TakeCount).ToList();
            }

            return concurents.AsEnumerable();
        }


        private IEnumerable<ConcurentResultDTO> GetConcurents(ConcurentModel model, IEnumerable<UserProfile> allStudents, Func<int, int, int, int, int> GetTotalRatingByBranchDisciplineFunc, int divisionId)
        {
            List<ConcurentResultDTO> concurents = new List<ConcurentResultDTO>();
            double total = GetTotalRatingByBranchDisciplineFunc(model.StudentId, model.DisciplineBranchId.Value, model.DisciplineId.Value, divisionId);

            if (model.SkipCount < 0)
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchDisciplineFunc(u.Id, model.DisciplineBranchId.Value, model.DisciplineId.Value, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue > total).OrderBy(c => c.RatingValue).Skip(Math.Abs(model.SkipCount + model.TakeCount)).Take(model.TakeCount).OrderByDescending(c => c.RatingValue).ToList();
            }
            else
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchDisciplineFunc(u.Id, model.DisciplineBranchId.Value, model.DisciplineId.Value, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue <= total).OrderByDescending(c => c.RatingValue).Skip(model.SkipCount).Take(model.TakeCount).ToList();
            }

            return concurents.AsEnumerable();
        }

        private IEnumerable<ConcurentResultDTO> GetConcurents(ConcurentModel model, IEnumerable<UserProfile> allStudents, Func<int, int, int, int> GetTotalRatingByBranchDisciplineFunc)
        {
            List<ConcurentResultDTO> concurents = new List<ConcurentResultDTO>();
            double total = GetTotalRatingByBranchDisciplineFunc(model.StudentId, model.DisciplineBranchId.Value, model.DisciplineId.Value);

            if (model.SkipCount < 0)
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchDisciplineFunc(u.Id, model.DisciplineBranchId.Value, model.DisciplineId.Value) });
                }
                concurents = concurents.Where(c => c.RatingValue > total).OrderBy(c => c.RatingValue).Skip(Math.Abs(model.SkipCount + model.TakeCount)).Take(model.TakeCount).OrderByDescending(c => c.RatingValue).ToList();
            }
            else
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchDisciplineFunc(u.Id, model.DisciplineBranchId.Value, model.DisciplineId.Value) });
                }
                concurents = concurents.Where(c => c.RatingValue <= total).OrderByDescending(c => c.RatingValue).Skip(model.SkipCount).Take(model.TakeCount).ToList();
            }

            return concurents.AsEnumerable();
        }

        private IEnumerable<ConcurentResultDTO> GetConcurents(ConcurentModel model, IEnumerable<UserProfile> allStudents, Func<int, int, int, int, double> GetTotalRatingByBranchDisciplineFunc, int divisionId)
        {
            List<ConcurentResultDTO> concurents = new List<ConcurentResultDTO>();
            double total = GetTotalRatingByBranchDisciplineFunc(model.StudentId, model.DisciplineBranchId.Value, model.DisciplineId.Value, divisionId);

            if (model.SkipCount < 0)
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchDisciplineFunc(u.Id, model.DisciplineBranchId.Value, model.DisciplineId.Value, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue > total).OrderBy(c => c.RatingValue).Skip(Math.Abs(model.SkipCount + model.TakeCount)).Take(model.TakeCount).OrderByDescending(c => c.RatingValue).ToList();
            }
            else
            {
                foreach (var u in allStudents)
                {
                    concurents.Add(new ConcurentResultDTO { UserProfile = u, RatingValue = GetTotalRatingByBranchDisciplineFunc(u.Id, model.DisciplineBranchId.Value, model.DisciplineId.Value, divisionId) });
                }
                concurents = concurents.Where(c => c.RatingValue <= total).OrderByDescending(c => c.RatingValue).Skip(model.SkipCount).Take(model.TakeCount).ToList();
            }

            return concurents.AsEnumerable();
        }

    }
}

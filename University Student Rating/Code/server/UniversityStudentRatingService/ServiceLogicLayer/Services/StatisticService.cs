using DataAccessLayer.DbContext;
using DataAccessLayer.Interfaces;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Services
{
    public class StatisticService : IStatisticService
    {
        IUnitOfWork Database { get; set; }

        public StatisticService(IUnitOfWork uow)
        {
            Database = uow;
        }

        public IEnumerable<DisciplineBranch> GetDisciplineBranches()
        {
            return Database.DisciplineBranchRepository.GetAll();
        }

        public IEnumerable<Discipline> GetDisciplinesByBranch(int branchId)
        {
            return Database.DisciplineRepository.Find(d => d.BranchId == branchId);
        }

        public IEnumerable<Rating> GetRatingBy(int studentId, int ratingDivisionId)
        {
            return Database.RatingRepository.Find(r => r.StudentId == studentId && r.RatingDivisionId == ratingDivisionId);
        }

        public IEnumerable<Rating> GetRatingBy(int studentId, int ratingDivisionId, int branchId)
        {
            return Database.RatingRepository.Find(r => r.StudentId == studentId && r.RatingDivisionId == ratingDivisionId && r.Discipline.BranchId == branchId);
        }

        public IEnumerable<Rating> GetRatingsForProfessorByDiscipline(int professorId, int disciplineId, int skipCount, int takeCount, IUserService userService)
        {
            List<Rating> ratings = Database.RatingRepository.Get(r => r.ProfessorId == professorId && r.DisciplineId == disciplineId, r => r.Id, skipCount, takeCount, false).ToList();

            foreach(var r in ratings)
            {
                r.Discipline = null;
                r.RatingDivision = null;
                r.UserProfile = userService.GetById(r.StudentId.Value);
            }
            return ratings;
        }


        public double GetAverageRatingByDivision(int studentId, int ratingDivisionId)
        {
            try
            {
                double average = Database.RatingRepository.Average(r => r.StudentId == studentId
                    && r.RatingDivisionId == ratingDivisionId, r => r.Points);
                return Math.Round(average, 2);
            }
            catch
            {
                return 0;
            }
        }

        public double GetAverageRatingByBranchDivision(int studentId, int branchId, int ratingDivisionId)
        {
            try
            {
                double average = Database.RatingRepository.Average(r => r.StudentId == studentId
                    && r.Discipline.BranchId == branchId
                    && r.RatingDivisionId == ratingDivisionId, r => r.Points);
                return Math.Round(average, 2);
            }
            catch
            {
                return 0;
            }
        }

        public double GetAverageRatingByBranchDisciplineDivision(int studentId, int branchId, int disciplineId, int ratingDivisionId)
        {
            try
            {
                double average = Database.RatingRepository.Average(r => r.StudentId == studentId
                    && r.Discipline.BranchId == branchId
                    && r.DisciplineId == disciplineId
                    && r.RatingDivisionId == ratingDivisionId, r => r.Points);
                return Math.Round(average, 2);
            }
            catch
            {
                return 0;
            }
        }

        public double GetAverageRatingForProfessor(int professorId)
        {
            try
            {
                double average = Database.RatingRepository.Average(r => r.ProfessorId == professorId && r.RatingDivisionId == 1, r => r.Points);
                return Math.Round(average, 2);
            }
            catch
            {
                return 0;
            }
        }


        public int GetTotalRating(int studentId)
        {
            try
            {
                int positive = Database.RatingRepository.Sum(r => r.StudentId == studentId && r.RatingDivisionId != 3, r => r.Points);
                int negative = Database.RatingRepository.Sum(r => r.StudentId == studentId && r.RatingDivisionId == 3, r => r.Points);
                return positive - negative;
            }
            catch
            {
                return 0;
            }
        }

        public int GetTotalRatingByBranch(int studentId, int branchId)
        {
            try
            {
                int positive = Database.RatingRepository.Sum(r => r.StudentId == studentId && r.RatingDivisionId != 3 && r.Discipline.BranchId == branchId, r => r.Points);
                int negative = Database.RatingRepository.Sum(r => r.StudentId == studentId && r.RatingDivisionId == 3 && r.Discipline.BranchId == branchId, r => r.Points);
                return positive - negative;
            }
            catch
            {
                return 0;
            }
        }

        public int GetTotalRatingByBranchDiscipline(int studentId, int branchId, int disciplineId)
        {
            try
            {
                int positive = Database.RatingRepository.Sum(r => r.StudentId == studentId && r.RatingDivisionId != 3 && 
                    r.DisciplineId == disciplineId &&
                    r.Discipline.BranchId == branchId, r => r.Points);
                int negative = Database.RatingRepository.Sum(r => r.StudentId == studentId && r.RatingDivisionId == 3 &&
                    r.DisciplineId == disciplineId &&
                    r.Discipline.BranchId == branchId, r => r.Points);
                return positive - negative;
            }
            catch
            {
                return 0;
            }
        }

        public int GetTotalRatingByDivision(int studentId, int divisionId)
        {
            try
            {
                int positive = Database.RatingRepository.Sum(r => r.StudentId == studentId && r.RatingDivision.Id == divisionId, r => r.Points);
                return positive;
            }
            catch
            {
                return 0;
            }
        }

        public int GetTotalRatingByBranchDivision(int studentId, int branchId, int divisionId)
        {
            try
            {
                int positive = Database.RatingRepository.Sum(r => r.StudentId == studentId && r.RatingDivision.Id == divisionId && r.Discipline.BranchId == branchId, r => r.Points);
                return positive;
            }
            catch
            {
                return 0;
            }
        }

        public int GetTotalRatingByBranchDisciplineDivision(int studentId, int branchId, int disciplineId, int divisionId)
        {
            try
            {
                int positive = Database.RatingRepository.Sum(r => r.StudentId == studentId && 
                    r.RatingDivision.Id == divisionId && 
                    r.DisciplineId == disciplineId &&
                    r.Discipline.BranchId == branchId, r => r.Points);
                
                return positive;
            }
            catch
            {
                return 0;
            }
        }


        public int GetMaxRating(int courseId)
        {
            try
            {
                int maxRating = Database.UserRepository.Find(u => u.StatusId == 1 && u.CourseId == courseId).Max(u => GetTotalRating(u.Id));
                return maxRating;
            }
            catch
            {
                return 0;
            }
        }

        public int GetMaxOlympiadsRating(int courseId)
        {
            try
            {
                int maxRating = Database.RatingRepository.Max(r => r.RatingDivision.Id == 2 
                    && r.UserProfile.CourseId == courseId, r => r.Points);
                return maxRating;
            }
            catch
            {
                return 0;
            }
        }

        public int GetMaxRatingByBranch(int courseId, int branchId)
        {
            try
            {
                int maxRating = Database.UserRepository.Find(u => u.StatusId == 1
                    && u.CourseId == courseId).Max(u => GetTotalRatingByBranch(u.Id, branchId));
                return maxRating;
            }
            catch
            {
                return 0;
            }
        }


        public int GetMaxRating(int universityId, int courseId)
        {
            try
            {
                int maxRating = Database.UserRepository.Find(u => u.StatusId == 1 && u.UniversityId == universityId && u.CourseId == courseId).Max(u => GetTotalRating(u.Id));
                return maxRating;
            }
            catch
            {
                return 0;
            }
        }

        public int GetMaxOlympiadsRating(int universityId, int courseId)
        {
            try
            {
                int maxRating = Database.RatingRepository.Max(r => r.RatingDivision.Id == 2
                     && r.UserProfile.UniversityId == universityId
                    && r.UserProfile.CourseId == courseId, r => r.Points);
                return maxRating;
            }
            catch
            {
                return 0;
            }
        }

        public int GetMaxRatingByBranch(int universityId, int courseId, int branchId)
        {
            try
            {
                int maxRating = Database.UserRepository.Find(u => u.StatusId == 1
                     && u.UniversityId == universityId
                    && u.CourseId == courseId).Max(u => GetTotalRatingByBranch(u.Id, branchId));
                return maxRating;
            }
            catch
            {
                return 0;
            }
        }


        public ProfileRatingDTO GetProfileRating(int studentId, int courseId)
        {
            UserProfile userProfile = Database.UserRepository.Get(studentId);
            ProfileRatingDTO rating = new ProfileRatingDTO();
            rating.AverageClassRating = GetAverageRatingByDivision(studentId, 1);
            rating.MaxArtRating = GetMaxRatingByBranch(userProfile.UniversityId, courseId, 4);
            rating.MaxOlympiadsRating = GetMaxOlympiadsRating(userProfile.UniversityId, courseId);
            rating.MaxScientificRating = GetMaxRatingByBranch(userProfile.UniversityId, courseId, 2);
            rating.MaxSocietyRating = GetMaxRatingByBranch(userProfile.UniversityId, courseId, 5);
            rating.MaxSportRating = GetMaxRatingByBranch(userProfile.UniversityId, courseId, 1);
            rating.TotalArtRating = GetTotalRatingByBranch(studentId, 4);
            rating.TotalClassRating = GetTotalRatingByDivision(studentId, 1);
            rating.TotalOlympiadsRating = GetTotalRatingByDivision(studentId, 2);
            rating.TotalScientificRating = GetTotalRatingByBranch(studentId, 2);
            rating.TotalSocietyRating = GetTotalRatingByBranch(studentId, 5);
            rating.TotalSportRating = GetTotalRatingByBranch(studentId, 1);

            return rating;
        }

        public IndividualRatingDTO GetIndividualRating(int studentId, int disciplineId, string academicYear)
        {
            IndividualRatingDTO rating = new IndividualRatingDTO();
            string[] years = academicYear.Split(new []{'/'});
            int year1 = int.Parse(years[0]);
            int year2 = int.Parse(years[1]);

            rating.UserProfile = Database.UserRepository.Get(studentId);
            rating.Discipline = Database.DisciplineRepository.Get(disciplineId);

            //Classwork rating
            try
            {
                rating.AverageClassRating = Database.RatingRepository.Average(r => r.StudentId == studentId
                    && r.DisciplineId == disciplineId
                    && r.RatingDivision.Id == 1
                    && (r.Date.Year == year1 || r.Date.Year == year2),
                    r => r.Points);
            }
            catch
            {
                rating.AverageClassRating = 0;
            }
            try
            {
                rating.TotalClassRating = Database.RatingRepository.Sum(r => r.StudentId == studentId
                    && r.DisciplineId == disciplineId
                    && r.RatingDivision.Id == 1
                    && (r.Date.Year == year1 || r.Date.Year == year2),
                    r => r.Points);
            }
            catch
            {
                rating.TotalClassRating = 0;
            }
            rating.ClassRatings = Database.RatingRepository.Find(r => r.StudentId == studentId
                && r.DisciplineId == disciplineId
                && r.RatingDivision.Id == 1
                && (r.Date.Year == year1 || r.Date.Year == year2)).ToList();
            //Total olympiads rating
            try
            {
                rating.TotalOlympiadsRating = Database.RatingRepository.Sum(r => r.StudentId == studentId
                    && r.DisciplineId == disciplineId
                    && r.RatingDivision.Id == 2
                    && (r.Date.Year == year1 || r.Date.Year == year2),
                    r => r.Points);
            }
            catch
            {
                rating.TotalOlympiadsRating = 0;
            }
            rating.OlympiadsRatings = Database.RatingRepository.Find(r => r.StudentId == studentId
                && r.DisciplineId == disciplineId
                && r.RatingDivision.Id == 2
                && (r.Date.Year == year1 || r.Date.Year == year2)).ToList();
            //skipping rating
            try
            {
                rating.TotalScippingClassRating = Database.RatingRepository.Sum(r => r.StudentId == studentId
                    && r.DisciplineId == disciplineId
                    && r.RatingDivision.Id == 3
                    && (r.Date.Year == year1 || r.Date.Year == year2),
                    r => r.Points);
            }
            catch
            {
                rating.TotalScippingClassRating = 0;
            }
            rating.ScippingClassRatings = Database.RatingRepository.Find(r => r.StudentId == studentId
                && r.DisciplineId == disciplineId
                && r.RatingDivision.Id == 3
                && (r.Date.Year == year1 || r.Date.Year == year2)).ToList();

            return rating;
        }

        public IndividualRatingDTO GetIndividualRating(int studentId, int disciplineId)
        {
            IndividualRatingDTO rating = new IndividualRatingDTO();

            rating.UserProfile = Database.UserRepository.Get(studentId);
            rating.Discipline = Database.DisciplineRepository.Get(disciplineId);

            //Classwork rating
            try
            {
                rating.AverageClassRating = Database.RatingRepository.Average(r => r.StudentId == studentId
                    && r.DisciplineId == disciplineId
                    && r.RatingDivision.Id == 1,
                    r => r.Points);
            }
            catch
            {
                rating.AverageClassRating = 0;
            }
            try
            {
                rating.TotalClassRating = Database.RatingRepository.Sum(r => r.StudentId == studentId
                    && r.DisciplineId == disciplineId
                    && r.RatingDivision.Id == 1,
                    r => r.Points);
            }
            catch
            {
                rating.TotalClassRating = 0;
            }
            rating.ClassRatings = Database.RatingRepository.Find(r => r.StudentId == studentId
                && r.DisciplineId == disciplineId
                && r.RatingDivision.Id == 1).ToList();
            //Total olympiads rating
            try
            {
                rating.TotalOlympiadsRating = Database.RatingRepository.Sum(r => r.StudentId == studentId
                    && r.DisciplineId == disciplineId
                    && r.RatingDivision.Id == 2,
                    r => r.Points);
            }
            catch
            {
                rating.TotalOlympiadsRating = 0;
            }
            rating.OlympiadsRatings = Database.RatingRepository.Find(r => r.StudentId == studentId
                && r.DisciplineId == disciplineId
                && r.RatingDivision.Id == 2).ToList();
            //skipping rating
            try
            {
                rating.TotalScippingClassRating = Database.RatingRepository.Sum(r => r.StudentId == studentId
                    && r.DisciplineId == disciplineId
                    && r.RatingDivision.Id == 3,
                    r => r.Points);
            }
            catch
            {
                rating.TotalScippingClassRating = 0;
            }
            rating.ScippingClassRatings = Database.RatingRepository.Find(r => r.StudentId == studentId
                && r.DisciplineId == disciplineId
                && r.RatingDivision.Id == 3).ToList();

            return rating;
        }

        public List<IndividualRatingDTO> GetAverageClassTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount)
        {
            List<IndividualRatingDTO> individualRatings = new List<IndividualRatingDTO>();

            List<UserProfile> topStudents = Database.UserRepository.Get(u => u.UniversityId == universityId
                && u.CourseId == courseId,
                u => u.Ratings.Where(r => r.DisciplineId == disciplineId && r.RatingDivision.Id == 1).Average(r => r.Points), 0, topCount, false).ToList();

            foreach (var s in topStudents)
            {
                individualRatings.Add(GetIndividualRating(s.Id, disciplineId));
            }
            return individualRatings;
        }

        public List<IndividualRatingDTO> GetOverallTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount)
        {
            List<IndividualRatingDTO> individualRatings = new List<IndividualRatingDTO>();

            List<UserProfile> topStudents = Database.UserRepository.Get(u => u.UniversityId == universityId
                && u.CourseId == courseId,
                u => u.Ratings.Where(r => r.DisciplineId == disciplineId).Sum(r => r.Points), 0, topCount, false).ToList();

            foreach (var s in topStudents)
            {
                individualRatings.Add(GetIndividualRating(s.Id, disciplineId));
            }
            return individualRatings;
        }

        public List<IndividualRatingDTO> GetOlympiadsTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount)
        {
            List<IndividualRatingDTO> individualRatings = new List<IndividualRatingDTO>();

            List<UserProfile> topStudents = Database.UserRepository.Get(u => u.UniversityId == universityId
                && u.CourseId == courseId,
                u => u.Ratings.Where(r => r.DisciplineId == disciplineId && r.RatingDivision.Id == 2).Sum(r => r.Points), 0, topCount, false).ToList();

            foreach (var s in topStudents)
            {
                individualRatings.Add(GetIndividualRating(s.Id, disciplineId));
            }
            return individualRatings;
        }

        public RatingType GetRatingType(int universityId, int disciplineId)
        {
            RatingSystem ratingSystem = Database.RatingSystemRepository.Find(rs => rs.UniversityId == universityId && rs.DisciplineId == disciplineId).FirstOrDefault();
            if (ratingSystem != null)
            {
                return Database.RatingTypeRepository.Get(ratingSystem.RatingTypeId);
            }
            return Database.RatingTypeRepository.Get(1);
        }


        public bool SetRating(Rating rating)
        {
            try
            {
                rating.Date = DateTime.Now;
                Database.RatingRepository.Create(rating);
                Database.Save();
                return true;
            }
            catch
            {
                return false;
            }
        }

        public bool DeleteRatingByIds(List<int> ratingIds)
        {
            try
            {
                foreach (var id in ratingIds)
                {
                    Database.RatingRepository.Delete(id);
                }
                Database.Save();
                return true;
            }
            catch
            {
                return false;
            }
        }
    }
}

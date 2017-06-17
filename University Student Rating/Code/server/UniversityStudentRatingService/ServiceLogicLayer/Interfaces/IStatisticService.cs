using DataAccessLayer.DbContext;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Interfaces
{
    public interface IStatisticService
    {
        IEnumerable<DisciplineBranch> GetDisciplineBranches();
        IEnumerable<Discipline> GetDisciplinesByBranch(int branchId);
        IEnumerable<Rating> GetRatingBy(int studentId, int ratingDivisionId, int branchId);
        IEnumerable<Rating> GetRatingBy(int studentId, int ratingDivisionId);
        IEnumerable<Rating> GetRatingsForProfessorByDiscipline(int professorId, int disciplineId, int skipCount, int takeCount, IUserService userService);

        double GetAverageRatingByDivision(int studentId, int ratingDivisionId);
        double GetAverageRatingByBranchDivision(int studentId, int branchId, int ratingDivisionId);
        double GetAverageRatingByBranchDisciplineDivision(int studentId, int branchId, int disciplineId, int ratingDivisionId);
        double GetAverageRatingForProfessor(int professorId);

        int GetTotalRating(int studentId);
        int GetTotalRatingByBranch(int studentId, int branchId);
        int GetTotalRatingByBranchDiscipline(int studentId, int branchId, int disciplineId);
        int GetTotalRatingByDivision(int studentId, int divisionId);
        int GetTotalRatingByBranchDivision(int studentId, int branchId, int divisionId);
        int GetTotalRatingByBranchDisciplineDivision(int studentId, int branchId, int disciplineId, int divisionId);

        int GetMaxRating(int courseId);
        int GetMaxOlympiadsRating(int courseId);
        int GetMaxRatingByBranch(int courseId, int branchId);
        
        ProfileRatingDTO GetProfileRating(int studentId, int courseId);
        IndividualRatingDTO GetIndividualRating(int studentId, int disciplineId, string academicYear);
        IndividualRatingDTO GetIndividualRating(int studentId, int disciplineId);
        List<IndividualRatingDTO> GetAverageClassTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount);
        List<IndividualRatingDTO> GetOverallTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount);
        List<IndividualRatingDTO> GetOlympiadsTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount);
        RatingType GetRatingType(int universityId, int disciplineId);
        bool SetRating(Rating model);
        bool DeleteRatingByIds(List<int> ratingIds);
    }
}
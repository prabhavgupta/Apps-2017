using DataAccessLayer.DbContext;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Models;
using ServiceLogicLayer.Models.AccountModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Interfaces
{
    public interface ISearchService
    {
        IEnumerable<UserProfile> SearchUsers(SearchModel model);
        IEnumerable<ConcurentResultDTO> SearchConcurents(ConcurentModel model, IStatisticService statisticService);
    }
}
